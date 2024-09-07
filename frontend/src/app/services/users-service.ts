import { Injectable } from "@angular/core";
import { catchError, defer, delay, Observable, of, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { Role } from "./auth-service";
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";

type User = {
    name: string
}

class UserExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UserExistsError';
    }
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

export type UserDetails = {
    id: string,
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
    role: Role,
}

@Injectable({
    providedIn: 'root'
})
export class UsersService {

    private usersUrl = 'http://localhost:8080/api/v1/users';

    private meUrl = 'http://localhost:8080/api/v1/users/me';

    constructor(private http: HttpClient) { }

    getMe(token: string): Observable<User> {
        const headers = {
            'Authorization': `Bearer ${token}`,
        };

        return this.http.get<User>(this.meUrl, { headers });
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
                    return throwError(() => new Error('User already exists.'));
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