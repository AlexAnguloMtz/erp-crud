import { Injectable } from "@angular/core";
import { defer, delay, Observable, of, throwError } from "rxjs";

type LoginCredentials = {
    email: string,
    password: string,
}

type User = {
    name: string
}

export type Role = {
    id: string
    description: string,
}

class UnauthorizedError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'UnauthorizedError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    logIn(credentials: LoginCredentials): Observable<string> {
        if (credentials.email !== 'abarrey_root@gmail.com' || credentials.password !== '12345678') {
            return defer(() => {
                return throwError(() => new UnauthorizedError("UnauthorizedError")).pipe(delay(2000));
            });
        }
        return of('fake-jwt-token')
            .pipe(delay(2000));
        // return defer(() => {
        //    return throwError(() => new UserExistsError("UserExists")).pipe(delay(2000));
        //});
    }

    getUserData(token: string): Observable<User> {
        return of({ name: 'Usuario Raíz' }).pipe(delay(2000));
    }

    getRoles(): Observable<Array<Role>> {
        return of([
            {
                id: '1',
                description: 'Super Usuario',
            },
            {
                id: '2',
                description: 'Administrador',
            },
            {
                id: '3',
                description: 'Gerente',
            },
            {
                id: '4',
                description: 'Usuario',
            },
        ]).pipe(delay(2000));
    }
}