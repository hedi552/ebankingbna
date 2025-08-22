import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVirement, NewVirement } from '../virement.model';

export type PartialUpdateVirement = Partial<IVirement> & Pick<IVirement, 'id'>;

export type EntityResponseType = HttpResponse<IVirement>;
export type EntityArrayResponseType = HttpResponse<IVirement[]>;

@Injectable({ providedIn: 'root' })
export class VirementService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/virements');

  create(virement: NewVirement): Observable<EntityResponseType> {
    return this.http.post<IVirement>(this.resourceUrl, virement, { observe: 'response' });
  }

  update(virement: IVirement): Observable<EntityResponseType> {
    return this.http.put<IVirement>(`${this.resourceUrl}/${this.getVirementIdentifier(virement)}`, virement, { observe: 'response' });
  }

  partialUpdate(virement: PartialUpdateVirement): Observable<EntityResponseType> {
    return this.http.patch<IVirement>(`${this.resourceUrl}/${this.getVirementIdentifier(virement)}`, virement, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVirement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVirement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVirementIdentifier(virement: Pick<IVirement, 'id'>): number {
    return virement.id;
  }

  compareVirement(o1: Pick<IVirement, 'id'> | null, o2: Pick<IVirement, 'id'> | null): boolean {
    return o1 && o2 ? this.getVirementIdentifier(o1) === this.getVirementIdentifier(o2) : o1 === o2;
  }

  addVirementToCollectionIfMissing<Type extends Pick<IVirement, 'id'>>(
    virementCollection: Type[],
    ...virementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const virements: Type[] = virementsToCheck.filter(isPresent);
    if (virements.length > 0) {
      const virementCollectionIdentifiers = virementCollection.map(virementItem => this.getVirementIdentifier(virementItem));
      const virementsToAdd = virements.filter(virementItem => {
        const virementIdentifier = this.getVirementIdentifier(virementItem);
        if (virementCollectionIdentifiers.includes(virementIdentifier)) {
          return false;
        }
        virementCollectionIdentifiers.push(virementIdentifier);
        return true;
      });
      return [...virementsToAdd, ...virementCollection];
    }
    return virementCollection;
  }
   getByCurrentUser(): Observable<EntityArrayResponseType> {
    return this.http.get<IVirement[]>(`${this.resourceUrl}/current-user`, { observe: 'response' });
  }
}