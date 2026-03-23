package com.renzozukeram.ecommerce.controllers

import com.renzozukeram.ecommerce.model.dto.CustomerRequest
import com.renzozukeram.ecommerce.model.dto.CustomerResponse
import com.renzozukeram.ecommerce.model.dto.CustomerUpdateRequest
import com.renzozukeram.ecommerce.model.dto.CustomerWithOrdersResponse
import com.renzozukeram.ecommerce.services.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(private val customerService: CustomerService) {

    @GetMapping
    fun findAll(
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): ResponseEntity<Page<CustomerResponse>> =
        ResponseEntity.ok(customerService.findAll(pageable))

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(customerService.findById(id))

    @GetMapping("/{id}/orders")
    fun findByIdWithOrders(@PathVariable id: UUID): ResponseEntity<CustomerWithOrdersResponse> =
        ResponseEntity.ok(customerService.findByIdWithOrders(id))

    @PostMapping
    fun create(@Valid @RequestBody request: CustomerRequest): ResponseEntity<CustomerResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: CustomerUpdateRequest
    ): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(customerService.update(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        customerService.delete(id)
        return ResponseEntity.noContent().build()
    }
}