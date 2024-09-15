import { Injectable } from "@angular/core";

const TOKEN_KEY: string = 'auth-token';

export type AuthenticationProof = {
    token: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthenticationProofVault {

    getAuthenticationProof(): AuthenticationProof | undefined {
        const token: string | null = localStorage.getItem(TOKEN_KEY);

        if (!token) {
            return undefined;
        }

        return {
            token,
        };
    }

    setAuthenticationProof(authenticationProof: AuthenticationProof) {
        localStorage.setItem(TOKEN_KEY, authenticationProof.token);
    }

    removeAuthenticationProof(): void {
        localStorage.removeItem(TOKEN_KEY);
    }

    hasValidAuthenticationProof(): boolean {
        return this.isValidAuthenticationProof(this.getAuthenticationProof());
    }

    isValidAuthenticationProof = (authenticationProof: AuthenticationProof | undefined): boolean => {
        if (!authenticationProof) {
            return false;
        }

        if (authenticationProof.token === null) {
            return false;
        }

        if (authenticationProof.token === '') {
            return false;
        }

        return true;
    }

}