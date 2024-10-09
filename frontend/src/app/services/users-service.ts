import { Injectable } from "@angular/core";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { Role } from "./auth-service";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { ApiClient } from "./api-client";
import { DataIntegrityError } from "../common/data-integrity-error";

type User = {
    name: string
}

class UserExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UserExistsError';
    }
}

export type Address = {
    id: number,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
}

export type CreateUserCommand = {
    name: string,
    lastName: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
    email: string,
    phone: string,
    roleId: string,
    password: string,
    confirmedPassword: string,
}

export type UpdateUserCommand = {
    name: string,
    lastName: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
    email: string,
    phone: string,
    roleId: string,
}

export type UserDetails = {
    id: number,
    name: string,
    lastName: string,
    email: string,
    phone: string,
    role: Role,
    address: Address,
}

export type UpdateUserResponse = {
    user: UserDetails,
    jwt: string | undefined
}

export type GetUsersParams = {
    roles?: Array<number>,
}

export class ForbiddenError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'ForbiddenError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class UsersService {

    private usersEndpoint = '/api/v1/users';

    private meEndpoint = '/api/v1/users/me';

    constructor(private apiClient: ApiClient) { }

    getMe(): Observable<User> {
        return this.apiClient.get<User>(this.meEndpoint).pipe(
            retry(5),
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Forbidden) {
                    return throwError(() => new ForbiddenError('ForbiddenError'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    getUsers(pagination: PaginatedRequest, params?: GetUsersParams): Observable<PaginatedResponse<UserDetails>> {
        return this.apiClient.get<PaginatedResponse<UserDetails>>(this.usersEndpoint, { params: { ...pagination, ...params } }).pipe(
            retry(5),
        );
    }

    createUser(command: CreateUserCommand): Observable<void> {
        return this.apiClient.post<void>(this.usersEndpoint, command).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new UserExistsError('User already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateUser(id: number, command: UpdateUserCommand): Observable<UpdateUserResponse> {
        const url = `${this.usersEndpoint}/${id}`;

        return this.apiClient.put<UpdateUserResponse>(url, command).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Conflict) {
                    return throwError(() => new UserExistsError('User already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    deleteUserById(id: number): Observable<void> {
        const url = `${this.usersEndpoint}/${id}`;
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