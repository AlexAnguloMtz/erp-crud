import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, defer, delay, Observable, of, switchMap, throwError } from "rxjs";
import { State } from "./users-service";


@Injectable({
    providedIn: 'root'
})
export class LocationsService {

    counter = 0

    private statesUrl = 'http://localhost:8080/api/v1/locations/states';

    constructor(private http: HttpClient) { }

    // getStates(token: string): Observable<Array<State>> {
    //     const headers = {
    //         'Authorization': `Bearer ${token}`
    //     }

    //     return this.http.get<Array<State>>(this.statesUrl, { headers }).pipe(
    //         catchError((error) => {
    //             return throwError(() => new Error());
    //         })
    //     );
    // }

    getStates(token: string): Observable<Array<State>> {
        this.counter = this.counter + 1
        if (this.counter === 4) {
            return of([{ name: 'Sonora', id: 'SON' }, { name: 'Ciudad de México', id: 'CDMX' }])
        }
        return of([])
            .pipe(
                delay(2000),
                switchMap(() => throwError(() => new Error('An error occurred')))
            );
    }
}