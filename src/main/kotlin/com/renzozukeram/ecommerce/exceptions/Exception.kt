package com.renzozukeram.ecommerce.exceptions

import java.util.*

class ResourceNotFoundException(message: String) : RuntimeException(message) {
    constructor(resource: String, id: UUID) : this("$resource not found with id: $id")
}

class BusinessException(message: String) : RuntimeException(message)

class DuplicateResourceException(message: String) : RuntimeException(message)