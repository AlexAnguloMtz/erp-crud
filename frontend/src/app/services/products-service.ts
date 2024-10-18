import { Injectable } from "@angular/core";
import { ApiClient } from "./api-client";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { catchError, Observable, retry, throwError } from "rxjs";
import { Brand } from "./brands-service";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";

export class ProductExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'ProductExistsError';
    }
}

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
    inventoryUnit: InventoryUnit
    image: string | undefined
}

export type InventoryUnit = {
    id: number
    name: string
}

export type ProductImageAction = 'none' | 'edit' | 'delete'

export type ProductCommand = {
    name: string
    sku: string
    salePrice: number
    brandId: number
    productCategoryId: number
    inventoryUnitId: number
    imageAction: ProductImageAction;
}

@Injectable({
    providedIn: 'root'
})
export class ProductsService {


    private productsEndpoint = '/api/v1/products';
    private productImagePath = this.productsEndpoint + '/product-image'

    constructor(private apiClient: ApiClient) { }

    getProducts(pagination: PaginatedRequest): Observable<PaginatedResponse<Product>> {
        return this.apiClient.get<PaginatedResponse<Product>>(this.productsEndpoint, { params: pagination }).pipe(
            retry(5),
        );
    }

    getProductImage(image: string): Observable<ArrayBuffer> {
        image = image.startsWith("/") ? image : "/" + image;
        return this.apiClient.get(this.productImagePath + image, { responseType: 'arraybuffer' });
    }

    createProduct(dto: ProductCommand, image: File | undefined): Observable<void> {
        const formData: FormData = new FormData();

        if (image) {
            formData.append('image', image, image.name);
        }

        formData.append('command', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

        return this.apiClient.post<void>(this.productsEndpoint, formData).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new ProductExistsError('Product already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateProduct(id: number, dto: ProductCommand, image: File | undefined): Observable<void> {
        const url = `${this.productsEndpoint}/${id}`;

        const formData: FormData = new FormData();

        if (image) {
            formData.append('image', image, image.name);
        }

        formData.append('command', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

        return this.apiClient.put<void>(url, formData).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new ProductExistsError('Product already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteProductById(id: number): Observable<void> {
        throw new Error('Method not implemented.');
    }

}