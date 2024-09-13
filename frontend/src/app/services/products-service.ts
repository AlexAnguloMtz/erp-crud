import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedRequest } from "../common/paginated-request";
import { PaginatedResponse } from "../common/paginated-response";

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

    private brandsUrl = environment.apiUrl + '/api/v1/brands';

    constructor(private http: HttpClient) { }

    getBrands(token: string, request: PaginatedRequest): Observable<PaginatedResponse<Brand>> {
        const headers = {
            'Authorization': `Bearer ${token}`
        }

        const queryString = paginatedRequestToQueryString(request);

        return this.http.get<PaginatedResponse<Brand>>(this.brandsUrl + queryString, { headers }).pipe(
            retry(5),
        );
    }

    createBrand(token: string, dto: BrandCommand): Observable<void> {
        const headers = {
            'Authorization': `Bearer ${token}`
        }

        return this.http.post<void>(this.brandsUrl, dto, { headers }).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === 409) {
                    return throwError(() => new BrandExistsError('Brand already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateBrand(token: string, id: string, command: BrandCommand): Observable<void> {
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        const url = `${this.brandsUrl}/${id}`;

        return this.http.put<void>(url, command, { headers }).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === 409) {
                    return throwError(() => new BrandExistsError('Brand already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteBrandById(token: string, id: string): Observable<void> {
        const headers = {
            'Authorization': `Bearer ${token}`,
        };

        const url = `${this.brandsUrl}/${id}`;

        return this.http.delete<void>(url, { headers });
    }
}

function paginatedRequestToQueryString(params: PaginatedRequest): string {
    const searchParams = new URLSearchParams();

    for (const [key, value] of Object.entries(params)) {
        if (value != null && value != '') {
            searchParams.append(key, value.toString());
        }
    }

    return searchParams.toString() ? `?${searchParams.toString()}` : '';
}