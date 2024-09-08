import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, defer, delay, Observable, of, switchMap, throwError } from "rxjs";

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

    private loginUrl = 'http://localhost:8080/api/v1/auth/login';

    private rolesUrl = 'http://localhost:8080/api/v1/auth/roles';

    constructor(private http: HttpClient) { }

    logIn(credentials: LoginCredentials): Observable<AuthenticationResponse> {
        return this.http.post<AuthenticationResponse>(this.loginUrl, credentials).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && error.status === 403) {
                    return throwError(() => new BadCredentialsError('BadCredentials'));
                }
                return throwError(() => new Error());
            })
        );
    }

    getRoles(token: string): Observable<Array<Role>> {
        const headers = { 'Authorization': `Bearer ${token}` }
        return this.http.get<Array<Role>>(this.rolesUrl, { headers });
    }
}