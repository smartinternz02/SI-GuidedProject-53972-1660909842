

package com.example.background

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.background.workers.BlurWorker
import com.example.background.workers.CleanupWorker
import com.example.background.workers.SaveImageToFileWorker


class BlurViewModel(application: Application) : ViewModel() {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)
    // New instance variable for the WorkInfo
    internal val outputWorkInfos: LiveData<List<WorkInfo>>


    init {
        imageUri = getImageUri(application.applicationContext)

        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    internal fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }



    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {

        // Add WorkRequest to Cleanup temporary images
//        var continuation = workManager
//                .beginWith(OneTimeWorkRequest
//                .from(CleanupWorker::class.java))

        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )



// Add WorkRequest to blur the image
//        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
//            .setInputData(createInputDataForUri())
//            .build()
//
////        workManager.enqueue(blurRequest)
//
//        continuation = continuation.then(blurRequest)


        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Add WorkRequest to save the image to the filesystem
//        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java).addTag(TAG_OUTPUT).build()

        // Create charging constraint
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()


        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()

        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()

    }


    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()

        return imageUri
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

    class BlurViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
                BlurViewModel(application) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
