import { Injectable } from "@angular/core";
import { catchError, Observable, retry, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { Role } from "./auth-service";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { ApiClient } from "./api-client";

type User = {
    name: string
}

class UserExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UserExistsError';
    }
}

export type State = {
    id: string,
    name: string,
}

export type Address = {
    id: string,
    state: State,
    city: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
}

export type CreateUserCommand = {
    name: string,
    lastName: string,
    state: string,
    city: string,
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
    state: string,
    city: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
    email: string,
    phone: string,
    roleId: string,
}

export type UserDetails = {
    id: string,
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
    states?: Array<string>,
    roles?: Array<string>,
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
        console.log('will send params ' + JSON.stringify({ ...pagination, ...params }))
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

    updateUser(id: string, command: UpdateUserCommand): Observable<UpdateUserResponse> {
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

    deleteUserById(id: string): Observable<void> {
        const url = `${this.usersEndpoint}/${id}`;
        return this.apiClient.delete<void>(url);
    }
}