import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { catchError, map, Observable, retry, throwError } from "rxjs";
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
    description: string;
}

export type Movement = {
    id: string;
    responsible: PersonalNameDTO;
    movementType: MovementTypeDTO;
    productQuantities: Array<ProductQuantityDTO>;
    observations: string;
    timestamp: Date;
}

export type GetMovementsParams = {
    responsibleId?: string;
    start?: Date;
    end?: Date;
    productId?: string;
}

@Injectable({
    providedIn: 'root'
})
export class MovementService {

    private movementsUrl = environment.apiUrl + '/api/v1/movements';

    constructor(private http: HttpClient) { }

    getMovements(token: string, pagination: PaginatedRequest, params: GetMovementsParams): Observable<PaginatedResponse<Movement>> {
        const headers = {
            'Authorization': `Bearer ${token}`
        }

        return this.http.get<PaginatedResponse<Movement>>(
            this.movementsUrl + '?' + toQueryString(pagination, params), { headers }
        ).pipe(
            retry(5),
            map(response => {
                return {
                    ...response,
                    items: response.items.map((x) => ({ ...x, timestamp: new Date(x.timestamp) }))
                };
            })
        );
    }
}

function toQueryString(pagination: PaginatedRequest, params: GetMovementsParams): string {
    const urlParams = new URLSearchParams();

    if (pagination.search) urlParams.append('search', pagination.search);
    if (pagination.pageNumber !== undefined) urlParams.append('pageNumber', pagination.pageNumber.toString());
    if (pagination.pageSize !== undefined) urlParams.append('pageSize', pagination.pageSize.toString());
    if (pagination.sort) urlParams.append('sort', pagination.sort);

    if (params.responsibleId) urlParams.append('responsibleId', params.responsibleId);
    if (params.start) urlParams.append('start', params.start.toISOString())
    if (params.end) urlParams.append('end', params.end.toISOString())
    if (params.productId) urlParams.append('productId', params.productId)

    return urlParams.toString();
}