import { Injectable } from "@angular/core";
import { map, Observable, retry } from "rxjs";
import { PaginatedRequest } from "../common/paginated-request";
import { PaginatedResponse } from "../common/paginated-response";
import { ApiClient } from "./api-client";

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

    private movementsEndpoint = '/api/v1/movements';

    constructor(private apiClient: ApiClient) { }

    getMovements(pagination: PaginatedRequest, params: GetMovementsParams): Observable<PaginatedResponse<Movement>> {
        return this.apiClient.get<PaginatedResponse<Movement>>(this.movementsEndpoint, { params: { ...pagination, ...params } })
            .pipe(
                retry(5),
                map(response => this.parsePaginatedResponse(response))
            );
    }

    private parsePaginatedResponse(response: PaginatedResponse<any>): PaginatedResponse<Movement> {
        return {
            ...response,
            items: response.items.map((x) => this.parseMovementJson(x))
        };
    }

    private parseMovementJson(json: any): Movement {
        return {
            ...json,
            timestamp: new Date(json.timestamp),
        };
    }
}