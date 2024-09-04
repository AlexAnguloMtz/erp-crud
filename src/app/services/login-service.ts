import { Injectable } from "@angular/core";
import { delay, Observable, of } from "rxjs";

type LoginCredentials = {
    username: string,
    password: string,
}

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    logIn(credentials: LoginCredentials): Observable<string> {
        return of('fake-jwt-token')
            .pipe(delay(2000));
    }

}