import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { Role } from "./auth-service";
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { environment } from "../../environments/environment";

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
    state: State,
    city: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
    email: string,
    phone: string,
    role: Role,
}

export type UpdateUserResponse = {
    user: UserDetails,
    jwt: string | undefined
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

    private usersUrl = environment.apiUrl + '/api/v1/users';

    private meUrl = environment.apiUrl + '/api/v1/users/me';

    constructor(private http: HttpClient) { }

    getMe(token: string): Observable<User> {
        const headers = {
            'Authorization': `Bearer ${token}`,
        };

        return this.http.get<User>(this.meUrl, { headers }).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === 403) {
                    return throwError(() => new ForbiddenError('ForbiddenError'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    getUsers(token: string, request: PaginatedRequest): Observable<PaginatedResponse<UserDetails>> {
        const headers = {
            'Authorization': `Bearer ${token}`,
        };

        const queryString = paginatedRequestToQueryString(request);

        return this.http.get<PaginatedResponse<UserDetails>>(this.usersUrl + queryString, { headers });
    }

    createUser(token: string, command: CreateUserCommand): Observable<void> {
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        return this.http.post<void>(this.usersUrl, command, { headers }).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === 409) {
                    return throwError(() => new UserExistsError('User already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
        );
    }

    updateUser(token: string, id: string, command: UpdateUserCommand): Observable<UpdateUserResponse> {
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        const url = `${this.usersUrl}/${id}`;

        return this.http.put<UpdateUserResponse>(url, command, { headers }).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && error.status === 409) {
                    return throwError(() => new UserExistsError('User already exists.'));
                }
                return throwError(() => new Error('An unexpected error occurred.'));
            })
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