import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedRequest } from "../common/paginated-request";
import { PaginatedResponse } from "../common/paginated-response";

export type Brand = {
    id: string,
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