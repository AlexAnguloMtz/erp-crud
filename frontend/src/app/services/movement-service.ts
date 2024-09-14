import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedRequest } from "../common/paginated-request";
import { PaginatedResponse } from "../common/paginated-response";

export type BrandDTO = {
    id: string;
    name: string;
}

export type ProductCategoryDTO = {
    id: string;
    name: string;
}

export type ProductDTO = {
    id: string;
    name: string;
    brand: BrandDTO;
    category: ProductCategoryDTO;
}

export type ProductQuantityDTO = {
    id: string;
    product: ProductDTO;
    quantity: number;
}

export type PersonalNameDTO = {
    id: string;
    name: string;
    lastName: string;
}

export type MovementTypeDTO = {
    id: string;
    name: string;
}

export type Movement = {
    id: string;
    responsible: PersonalNameDTO;
    movementType: MovementTypeDTO;
    productQuantities: Array<ProductQuantityDTO>;
    timestamp: Date;
}

@Injectable({
    providedIn: 'root'
})
export class MovementService {

    private movementsUrl = environment.apiUrl + '/api/v1/movements';

    constructor(private http: HttpClient) { }

    getMovements(token: string, request: PaginatedRequest): Observable<PaginatedResponse<Movement>> {
        const headers = {
            'Authorization': `Bearer ${token}`
        }

        const queryString = paginatedRequestToQueryString(request);

        return this.http.get<PaginatedResponse<Movement>>(this.movementsUrl + queryString, { headers }).pipe(
            // retry(5),
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