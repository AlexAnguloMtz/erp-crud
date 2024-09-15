import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, retry, throwError } from "rxjs";
import { ApiClient } from "./api-client";

type LoginCredentials = {
    email: string,
    password: string,
}

export type AuthenticationResponse = {
    accessToken: string
}

export type Role = {
    id: string
    description: string,
}

class BadCredentialsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'BadCredentialsError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private loginEndpoint = '/api/v1/auth/login';

    private rolesEndpoint = '/api/v1/auth/roles';

    constructor(private apiClient: ApiClient) { }

    logIn(credentials: LoginCredentials): Observable<AuthenticationResponse> {
        return this.apiClient.post<AuthenticationResponse>(this.loginEndpoint, credentials, { authentication: false }).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && error.status === 403) {
                    return throwError(() => new BadCredentialsError('BadCredentials'));
                }
                return throwError(() => new Error());
            })
        );
    }

    getRoles(): Observable<Array<Role>> {
        return this.apiClient.get<Array<Role>>(this.rolesEndpoint).pipe(
            retry(5)
        );
    }
}