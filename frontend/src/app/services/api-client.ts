import { Injectable } from "@angular/core";
import { AuthenticationProof, AuthenticationProofVault } from "./authentication-proof-vault";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

export type ApiClientOptions = {
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
        return this.http.get<T>(this.prepareUrl(url), { headers: this.makeHeaders(options) });
    }

    post<T>(url: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.post<T>(this.prepareUrl(url), body, { headers: this.makeHeaders(options) });
    }

    put<T>(url: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.put<T>(this.prepareUrl(url), body, { headers: this.makeHeaders(options) });
    }

    delete<T>(url: string, options?: ApiClientOptions): Observable<T> {
        return this.http.delete<T>(this.prepareUrl(url), { headers: this.makeHeaders(options) });
    }

    private makeHeaders(someOptions?: ApiClientOptions): { [key: string]: string } {
        const options: ApiClientOptions = someOptions || this.defaultApiClientOptions();

        const headers: { [key: string]: string } = {}

        if (options.authentication === true) {
            headers['Authorization'] = `Bearer ${this.getAccessToken()}`;
        }

        return headers;
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

    private defaultApiClientOptions(): ApiClientOptions {
        return {
            authentication: true,
        };
    }

}