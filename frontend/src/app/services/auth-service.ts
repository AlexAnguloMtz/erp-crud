import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, retry, throwError } from "rxjs";
import { environment } from "../../environments/environment";

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

    private loginUrl = environment.apiUrl + '/api/v1/auth/login';

    private rolesUrl = environment.apiUrl + '/api/v1/auth/roles';

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
        return this.http.get<Array<Role>>(this.rolesUrl, { headers }).pipe(
            retry(5)
        );
    }
}