import { Injectable } from "@angular/core";
import { AuthenticationProof, AuthenticationProofVault } from "./authentication-proof-vault";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../environments/environment";

// Accepts Date objects
type RawParams = { [param: string]: string | number | boolean | Date | ReadonlyArray<string | number | boolean | Date> }

// Does not accept Date objects
type ParsedParams = { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> }

export type ApiClientOptions = {
    params?: RawParams
    authentication?: boolean
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

    get<T>(url: string, options?: ApiClientOptions): Observable<T> {
        return this.http.get<T>(this.prepareUrl(url), { headers: this.makeHeaders(options), params: this.parseParams(options?.params) });
    }

    post<T>(url: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.post<T>(this.prepareUrl(url), body, { headers: this.makeHeaders(options), params: this.parseParams(options?.params) });
    }

    put<T>(url: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.put<T>(this.prepareUrl(url), body, { headers: this.makeHeaders(options), params: this.parseParams(options?.params) });
    }

    delete<T>(url: string, options?: ApiClientOptions): Observable<T> {
        return this.http.delete<T>(this.prepareUrl(url), { headers: this.makeHeaders(options), params: this.parseParams(options?.params) });
    }

    private makeHeaders(options?: ApiClientOptions): { [key: string]: string } {
        const headers: { [key: string]: string } = {}

        if (this.needsAuthentication(options)) {
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

    private prepareUrl(url: string): string {
        return environment.apiUrl + url;
    }

    private needsAuthentication(options?: ApiClientOptions) {
        if (!options) {
            return true;
        }

        if (options.authentication === undefined) {
            return true;
        }

        if (options.authentication === null) {
            return true;
        }

        return options.authentication;
    }

}