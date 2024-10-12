import { Injectable } from "@angular/core";
import { ApiClient } from "./api-client";
import { catchError, Observable, retry, throwError } from "rxjs";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { DataIntegrityError } from "../common/data-integrity-error";
import { BranchType } from "./branches-service";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";

class BranchTypeExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'BranchTypeExistsError';
    }
}

export type BranchTypeCommand = {
    name: string
    description: string
}

@Injectable({
    providedIn: 'root'
})
export class BranchTypesService {

    private branchTypesEndpoint = '/api/v1/branch-types';

    constructor(
        private apiClient: ApiClient,
    ) { }

    getBranchTypes(pagination: PaginatedRequest): Observable<PaginatedResponse<BranchType>> {
        return this.apiClient.get<PaginatedResponse<BranchType>>(this.branchTypesEndpoint, { params: pagination }).pipe(
            retry(5),
        );
    }

    getAllBranchTypes(): Observable<Array<BranchType>> {
        return this.apiClient.get<Array<BranchType>>(this.branchTypesEndpoint + '/all').pipe(
            retry(5),
        );
    }

    createBranchType(dto: BranchTypeCommand): Observable<void> {
        return this.apiClient.post<void>(this.branchTypesEndpoint, dto).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new BranchTypeExistsError('Branch Type already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateBranchType(id: number, command: BranchTypeCommand): Observable<void> {
        const url = `${this.branchTypesEndpoint}/${id}`;

        return this.apiClient.put<void>(url, command).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new BranchTypeExistsError('Branch Type already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteBranchTypeById(id: number): Observable<void> {
        const url = `${this.branchTypesEndpoint}/${id}`;

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