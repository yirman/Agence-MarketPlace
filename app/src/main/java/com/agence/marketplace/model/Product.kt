package com.agence.marketplace.model

import com.google.firebase.firestore.DocumentId

data class Product(
    @DocumentId var productId: String? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnailUrl: String? = null,
    var price: String? = null,

)