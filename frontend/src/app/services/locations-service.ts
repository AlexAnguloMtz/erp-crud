import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, defer, delay, Observable, of, throwError } from "rxjs";
import { State } from "./users-service";


@Injectable({
    providedIn: 'root'
})
export class LocationsService {

    private statesUrl = 'http://localhost:8080/api/v1/locations/states';

    constructor(private http: HttpClient) { }

    getStates(token: string): Observable<Array<State>> {
        const headers = {
            'Authorization': `Bearer ${token}`
        }

        return this.http.get<Array<State>>(this.statesUrl, { headers }).pipe(
            catchError((error) => {
                return throwError(() => new Error());
            })
        );
    }
}