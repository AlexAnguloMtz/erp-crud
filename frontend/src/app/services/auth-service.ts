import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
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
    name: string,
    canonicalName: string,
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
        return this.apiClient.post<AuthenticationResponse>(this.loginEndpoint, credentials).pipe(
            catchError((error) => this.handleLogInError(error))
        );
    }

    getRoles(): Observable<Array<Role>> {
        return this.apiClient.get<Array<Role>>(this.rolesEndpoint).pipe(
            retry(5)
        );
    }

    private handleLogInError(error: any): Observable<never> {
        if (error instanceof HttpErrorResponse && error.status === HttpStatusCode.Forbidden) {
            return throwError(() => new BadCredentialsError('BadCredentials'));
        }
        return throwError(() => new Error());
    }
}