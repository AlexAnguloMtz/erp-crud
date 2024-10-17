import { Injectable } from "@angular/core";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedRequest } from "../common/paginated-request";
import { PaginatedResponse } from "../common/paginated-response";
import { ApiClient } from "./api-client";
import { DataIntegrityError } from "../common/data-integrity-error";

class ProductCategoryExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'ProductCategoryExistsError';
    }
}

export type ProductCategory = {
    id: number,
    name: string
}

export type ProductCategoryCommand = {
    name: string
}

@Injectable({
    providedIn: 'root'
})
export class ProductCategoryService {

    private productCategoriesEndpoint = '/api/v1/product-categories';

    constructor(private apiClient: ApiClient) { }

    getProductCategories(pagination: PaginatedRequest): Observable<PaginatedResponse<ProductCategory>> {
        return this.apiClient.get<PaginatedResponse<ProductCategory>>(this.productCategoriesEndpoint, { params: pagination }).pipe(
            retry(5),
        );
    }

    getAllProductCategories(): Observable<Array<ProductCategory>> {
        return this.apiClient.get<Array<ProductCategory>>(this.productCategoriesEndpoint + "/all").pipe(
            retry(5),
        )
    }

    createProductCategory(dto: ProductCategoryCommand): Observable<void> {
        return this.apiClient.post<void>(this.productCategoriesEndpoint, dto).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new ProductCategoryExistsError('Product Category already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateProductCategory(id: number, command: ProductCategoryCommand): Observable<void> {
        const url = `${this.productCategoriesEndpoint}/${id}`;

        return this.apiClient.put<void>(url, command).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new ProductCategoryExistsError('Product Category already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteProductCategoryById(id: number): Observable<void> {
        const url = `${this.productCategoriesEndpoint}/${id}`;

        return this.apiClient.delete<void>(url).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.BadRequest) {
                    return throwError(() => new DataIntegrityError('Data integrity error'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

}