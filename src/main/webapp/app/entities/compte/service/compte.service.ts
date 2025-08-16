import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompte, NewCompte } from '../compte.model';

export type PartialUpdateCompte = Partial<ICompte> & Pick<ICompte, 'id'>;

export type EntityResponseType = HttpResponse<ICompte>;
export type EntityArrayResponseType = HttpResponse<ICompte[]>;

@Injectable({ providedIn: 'root' })
export class CompteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comptes');

  create(compte: NewCompte): Observable<EntityResponseType> {
    return this.http.post<ICompte>(this.resourceUrl, compte, { observe: 'response' });
  }

  update(compte: ICompte): Observable<EntityResponseType> {
    return this.http.put<ICompte>(`${this.resourceUrl}/${this.getCompteIdentifier(compte)}`, compte, { observe: 'response' });
  }

  partialUpdate(compte: PartialUpdateCompte): Observable<EntityResponseType> {
    return this.http.patch<ICompte>(`${this.resourceUrl}/${this.getCompteIdentifier(compte)}`, compte, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompteIdentifier(compte: Pick<ICompte, 'id'>): number {
    return compte.id;
  }

  compareCompte(o1: Pick<ICompte, 'id'> | null, o2: Pick<ICompte, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteIdentifier(o1) === this.getCompteIdentifier(o2) : o1 === o2;
  }

  addCompteToCollectionIfMissing<Type extends Pick<ICompte, 'id'>>(
    compteCollection: Type[],
    ...comptesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const comptes: Type[] = comptesToCheck.filter(isPresent);
    if (comptes.length > 0) {
      const compteCollectionIdentifiers = compteCollection.map(compteItem => this.getCompteIdentifier(compteItem));
      const comptesToAdd = comptes.filter(compteItem => {
        const compteIdentifier = this.getCompteIdentifier(compteItem);
        if (compteCollectionIdentifiers.includes(compteIdentifier)) {
          return false;
        }
        compteCollectionIdentifiers.push(compteIdentifier);
        return true;
      });
      return [...comptesToAdd, ...compteCollection];
    }
    return compteCollection;
  }
}
