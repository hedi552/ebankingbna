import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMondat, NewMondat } from '../mondat.model';

export type PartialUpdateMondat = Partial<IMondat> & Pick<IMondat, 'id'>;

export type EntityResponseType = HttpResponse<IMondat>;
export type EntityArrayResponseType = HttpResponse<IMondat[]>;

@Injectable({ providedIn: 'root' })
export class MondatService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mondats');

  create(mondat: NewMondat): Observable<EntityResponseType> {
    return this.http.post<IMondat>(this.resourceUrl, mondat, { observe: 'response' });
  }

  update(mondat: IMondat): Observable<EntityResponseType> {
    return this.http.put<IMondat>(`${this.resourceUrl}/${this.getMondatIdentifier(mondat)}`, mondat, { observe: 'response' });
  }

  partialUpdate(mondat: PartialUpdateMondat): Observable<EntityResponseType> {
    return this.http.patch<IMondat>(`${this.resourceUrl}/${this.getMondatIdentifier(mondat)}`, mondat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMondat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMondat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMondatIdentifier(mondat: Pick<IMondat, 'id'>): number {
    return mondat.id;
  }

  compareMondat(o1: Pick<IMondat, 'id'> | null, o2: Pick<IMondat, 'id'> | null): boolean {
    return o1 && o2 ? this.getMondatIdentifier(o1) === this.getMondatIdentifier(o2) : o1 === o2;
  }

  addMondatToCollectionIfMissing<Type extends Pick<IMondat, 'id'>>(
    mondatCollection: Type[],
    ...mondatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mondats: Type[] = mondatsToCheck.filter(isPresent);
    if (mondats.length > 0) {
      const mondatCollectionIdentifiers = mondatCollection.map(mondatItem => this.getMondatIdentifier(mondatItem));
      const mondatsToAdd = mondats.filter(mondatItem => {
        const mondatIdentifier = this.getMondatIdentifier(mondatItem);
        if (mondatCollectionIdentifiers.includes(mondatIdentifier)) {
          return false;
        }
        mondatCollectionIdentifiers.push(mondatIdentifier);
        return true;
      });
      return [...mondatsToAdd, ...mondatCollection];
    }
    return mondatCollection;
  }
  getByCurrentUser(): Observable<EntityArrayResponseType> {
      return this.http.get<IMondat[]>(`${this.resourceUrl}/current-user`, { observe: 'response' });
  }
}
