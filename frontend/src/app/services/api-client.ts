import { Injectable } from "@angular/core";
import { AuthenticationProof, AuthenticationProofVault } from "./authentication-proof-vault";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

const PUBLIC_PATHS: Array<string> = [
    '/api/v1/auth/login'
];

// Accepts Date objects
type RawParams = { [param: string]: string | number | boolean | Date | ReadonlyArray<string | number | boolean | Date> }

// Does not accept Date objects
type ParsedParams = { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> }

export type ApiClientOptions = {
    params?: RawParams
}

export class InvalidAuthenticationError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'InvalidAuthenticationError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class ApiClient {

    constructor(
        private http: HttpClient,
        private authenticationProofVault: AuthenticationProofVault,
    ) { }

    get<T>(path: string, options?: ApiClientOptions): Observable<T> {
        return this.http.get<T>(this.prepareUrl(path), { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    post<T>(path: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.post<T>(this.prepareUrl(path), body, { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    put<T>(path: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.put<T>(this.prepareUrl(path), body, { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    delete<T>(path: string, options?: ApiClientOptions): Observable<T> {
        return this.http.delete<T>(this.prepareUrl(path), { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    private prepareUrl(path: string): string {
        return environment.apiUrl + path;
    }

    private makeHeaders(path: string): { [key: string]: string } {
        const headers: { [key: string]: string } = {};

        if (!this.isPublicPath(path)) {
            headers['Authorization'] = `Bearer ${this.getAccessToken()}`;
        }

        return headers;
    }

    private parseParams(params?: RawParams): ParsedParams {
        if (!params) {
            return {};
        }
        // Parse all nested Dates as ISO strings
        return JSON.parse(JSON.stringify(params));
    }

    private getAccessToken(): string {
        const authenticationProof: AuthenticationProof | undefined = this.authenticationProofVault.getAuthenticationProof();

        if (!authenticationProof || !this.authenticationProofVault.isValidAuthenticationProof(authenticationProof)) {
            // TODO 
            // IMPORTANT: Catch this error SOMEWHERE and DO SOMETHING!!!
            throw new InvalidAuthenticationError("cannot use authentication proof because it is invalid");
        }

        return authenticationProof.token;
    }

    private isPublicPath(path: string) {
        return PUBLIC_PATHS.includes(path);
    }

}