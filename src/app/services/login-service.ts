import { Injectable } from "@angular/core";
import { defer, delay, Observable, of, throwError } from "rxjs";

type LoginCredentials = {
    username: string,
    password: string,
}

export class UnauthorizedError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UnauthorizedError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class LoginService {
    logIn(credentials: LoginCredentials): Observable<string> {
        // return of('fake-jwt-token')
        //     .pipe(delay(2000));
        return defer(() => {
            return throwError(() => new UnauthorizedError("Unauthorized")).pipe(delay(2000));
        });
    }
}