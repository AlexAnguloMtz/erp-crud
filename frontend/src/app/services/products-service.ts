import { Injectable } from "@angular/core";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedRequest } from "../common/paginated-request";
import { PaginatedResponse } from "../common/paginated-response";
import { ApiClient } from "./api-client";

class BrandExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'BrandExistsError';
    }
}

export type Brand = {
    id: string,
    name: string
}

export type BrandCommand = {
    name: string
}

@Injectable({
    providedIn: 'root'
})
export class ProductsService {

    private brandsEndpoint = '/api/v1/brands';

    constructor(private apiClient: ApiClient) { }

    getBrands(pagination: PaginatedRequest): Observable<PaginatedResponse<Brand>> {
        return this.apiClient.get<PaginatedResponse<Brand>>(this.brandsEndpoint, { params: pagination }).pipe(
            retry(5),
        );
    }

    createBrand(dto: BrandCommand): Observable<void> {
        return this.apiClient.post<void>(this.brandsEndpoint, dto).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new BrandExistsError('Brand already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateBrand(id: string, command: BrandCommand): Observable<void> {
        const url = `${this.brandsEndpoint}/${id}`;

        return this.apiClient.put<void>(url, command).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new BrandExistsError('Brand already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteBrandById(id: string): Observable<void> {
        const url = `${this.brandsEndpoint}/${id}`;
        return this.apiClient.delete<void>(url);
    }

}