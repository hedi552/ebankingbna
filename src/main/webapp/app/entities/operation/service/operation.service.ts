import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperation, NewOperation } from '../operation.model';

export type PartialUpdateOperation = Partial<IOperation> & Pick<IOperation, 'id'>;

export type EntityResponseType = HttpResponse<IOperation>;
export type EntityArrayResponseType = HttpResponse<IOperation[]>;

@Injectable({ providedIn: 'root' })
export class OperationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operations');

  create(operation: NewOperation): Observable<EntityResponseType> {
    return this.http.post<IOperation>(this.resourceUrl, operation, { observe: 'response' });
  }
  update(operation: IOperation): Observable<EntityResponseType> {
    return this.http.put<IOperation>(`${this.resourceUrl}/${this.getOperationIdentifier(operation)}`, operation, { observe: 'response' });
  }

  partialUpdate(operation: PartialUpdateOperation): Observable<EntityResponseType> {
    return this.http.patch<IOperation>(`${this.resourceUrl}/${this.getOperationIdentifier(operation)}`, operation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperationIdentifier(operation: Pick<IOperation, 'id'>): number {
    return operation.id;
  }

  compareOperation(o1: Pick<IOperation, 'id'> | null, o2: Pick<IOperation, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperationIdentifier(o1) === this.getOperationIdentifier(o2) : o1 === o2;
  }

  addOperationToCollectionIfMissing<Type extends Pick<IOperation, 'id'>>(
    operationCollection: Type[],
    ...operationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operations: Type[] = operationsToCheck.filter(isPresent);
    if (operations.length > 0) {
      const operationCollectionIdentifiers = operationCollection.map(operationItem => this.getOperationIdentifier(operationItem));
      const operationsToAdd = operations.filter(operationItem => {
        const operationIdentifier = this.getOperationIdentifier(operationItem);
        if (operationCollectionIdentifiers.includes(operationIdentifier)) {
          return false;
        }
        operationCollectionIdentifiers.push(operationIdentifier);
        return true;
      });
      return [...operationsToAdd, ...operationCollection];
    }
    return operationCollection;
  }
  getByCurrentUser(): Observable<EntityArrayResponseType> {
    return this.http.get<IOperation[]>(`${this.resourceUrl}/current-user`, { observe: 'response' });
  }
}