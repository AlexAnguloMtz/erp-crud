import { Injectable } from "@angular/core";
import { ApiClient } from "./api-client";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { DataIntegrityError } from "../common/data-integrity-error";

export type InventoryUnit = {
    id: number
    name: string
}

@Injectable({
    providedIn: 'root'
})
export class InventoryUnitService {

    private baseEndpoint = '/api/v1/inventory-units';

    constructor(private apiClient: ApiClient) { }

    getAllInventoryUnits(): Observable<Array<InventoryUnit>> {
        return this.apiClient.get<Array<InventoryUnit>>(this.baseEndpoint + "/all").pipe(
            retry(5),
        );
    }

}