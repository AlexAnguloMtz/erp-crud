import { Injectable } from "@angular/core";
import { defer, delay, Observable, of, throwError } from "rxjs";

type LoginCredentials = {
    username: string,
    password: string,
}

type User = {
    name: string
}

class UnauthorizedError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UnauthorizedError';
    }
}

class UserExistsError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UserExistsError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    logIn(credentials: LoginCredentials): Observable<string> {
        return of('fake-jwt-token')
            .pipe(delay(2000));
        // return defer(() => {
        //    return throwError(() => new UserExistsError("UserExists")).pipe(delay(2000));
        //});
    }

    getUserData(token: string): Observable<User> {
        return of({ name: 'Super User' }).pipe(delay(2000));
    }
}