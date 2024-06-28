package com.nadhifhayazee.domain.useCase.locationImage

import android.net.Uri
import javax.inject.Inject

class GenerateUriImagesUseCase @Inject constructor(
    private val verifyValidUriUseCase: VerifyValidUriUseCase
) {

    operator fun invoke(locationsUri: List<String>): List<Uri> {
        val locationImages = mutableListOf<Uri>()
        locationsUri.forEach { stringUri ->
            val uri = Uri.parse(stringUri)
            if (verifyValidUriUseCase(uri)) locationImages.add(uri)
        }
        return locationImages
    }
}