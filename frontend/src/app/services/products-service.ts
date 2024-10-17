import { Injectable } from "@angular/core";
import { ApiClient } from "./api-client";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { Observable, retry } from "rxjs";
import { Brand } from "./brands-service";

export type ProductCategory = {
    id: number,
    name: string
}

export type Product = {
    id: number
    name: string
    sku: string
    salePrice: number
    brand: Brand
    productCategory: ProductCategory
}

export type ProductCommand = {
    name: string,
    brandId: number,
    productCategoryId: number
}

@Injectable({
    providedIn: 'root'
})
export class ProductsService {


    private brandsEndpoint = '/api/v1/products';

    constructor(private apiClient: ApiClient) { }

    getProducts(pagination: PaginatedRequest): Observable<PaginatedResponse<Product>> {
        return this.apiClient.get<PaginatedResponse<Product>>(this.brandsEndpoint, { params: pagination }).pipe(
            retry(5),
        );
    }

    createProduct(dto: ProductCommand): Observable<void> {
        throw new Error('Method not implemented.');
    }

    updateProduct(id: number, dto: ProductCommand): Observable<void> {
        throw new Error('Method not implemented.');
    }

    deleteProductById(id: number): Observable<void> {
        throw new Error('Method not implemented.');
    }

}