import { Injectable } from "@angular/core";
import { Observable, retry } from "rxjs";
import { State } from "./users-service";
import { ApiClient } from "./api-client";

@Injectable({
    providedIn: 'root'
})
export class LocationsService {

    private statesEndpoint = '/api/v1/locations/states';

    constructor(private apiClient: ApiClient) { }

    getStates(): Observable<Array<State>> {
        return this.apiClient.get<Array<State>>(this.statesEndpoint).pipe(
            retry(5),
        );
    }
}