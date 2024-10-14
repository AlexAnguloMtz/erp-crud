import { Injectable } from "@angular/core";
import { ApiClient } from "./api-client";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { DataIntegrityError } from "../common/data-integrity-error";

class BranchExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'BranchExistsError';
    }
}

export type Branch = {
    id: number
    name: string
    phone: string
    image: string | undefined
    address: BranchAddress
    branchType: BranchType
}

export type BranchType = {
    id: number
    name: string
    description: string
}

export type BranchAddress = {
    id: number
    district: string
    street: string
    streetNumber: string
    zipCode: string
}

export type BranchImageAction = 'none' | 'edit' | 'delete'

export type BranchCommand = {
    name: string;
    phone: string;
    branchTypeId: number;
    district: string;
    street: string;
    streetNumber: string;
    zipCode: string;
    imageAction: BranchImageAction;
};

@Injectable({
    providedIn: 'root'
})
export class BranchesService {

    private branchesEndpoint = '/api/v1/branches';
    private branchImagePath = this.branchesEndpoint + '/branch-image';

    constructor(private apiClient: ApiClient) { }

    getBranches(pagination: PaginatedRequest): Observable<PaginatedResponse<Branch>> {
        return this.apiClient.get<PaginatedResponse<Branch>>(this.branchesEndpoint, { params: pagination }).pipe(
            retry(5),
        );
    }

    getBranchImage(image: string): Observable<ArrayBuffer> {
        image = image.startsWith("/") ? image : "/" + image;
        return this.apiClient.get(this.branchImagePath + image, { responseType: 'arraybuffer' });
    }

    createBranch(dto: BranchCommand, image: File | undefined): Observable<void> {
        const formData: FormData = new FormData();

        if (image) {
            formData.append('image', image, image.name);
        }

        formData.append('command', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

        return this.apiClient.post<void>(this.branchesEndpoint, formData).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new BranchExistsError('Branch already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateBranch(id: number, dto: BranchCommand, image: File | undefined): Observable<void> {
        const url = `${this.branchesEndpoint}/${id}`;

        const formData: FormData = new FormData();

        if (image) {
            formData.append('image', image, image.name);
        }

        formData.append('command', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

        return this.apiClient.put<void>(url, formData).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new BranchExistsError('Branch already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteBranchById(id: number): Observable<void> {
        const url = `${this.branchesEndpoint}/${id}`;

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