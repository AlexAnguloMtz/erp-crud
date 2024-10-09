import { Injectable } from "@angular/core";
import { ApiClient } from "./api-client";
import { map, Observable } from "rxjs";

export type BackupCreationResponse = {
    backup: string,
    createdAt: Date,
}

@Injectable({
    providedIn: 'root'
})
export class BackupsService {

    private brandsEndpoint = '/api/v1/backups';

    constructor(private apiClient: ApiClient) { }

    newBackup(): Observable<BackupCreationResponse> {
        return this.apiClient.get<BackupCreationResponse>(this.brandsEndpoint).pipe(
            map((json: any) => ({
                backup: json.backup,
                createdAt: new Date(json.createdAt),
            }))
        );
    }
}