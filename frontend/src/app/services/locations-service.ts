import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { State } from "./users-service";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class LocationsService {

    private statesUrl = environment.apiUrl + '/api/v1/locations/states';

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