import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMondat } from '../mondat.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../mondat.test-samples';

import { MondatService } from './mondat.service';

const requireRestSample: IMondat = {
  ...sampleWithRequiredData,
};

describe('Mondat Service', () => {
  let service: MondatService;
  let httpMock: HttpTestingController;
  let expectedResult: IMondat | IMondat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MondatService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Mondat', () => {
      const mondat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mondat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mondat', () => {
      const mondat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mondat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mondat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mondat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mondat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMondatToCollectionIfMissing', () => {
      it('should add a Mondat to an empty array', () => {
        const mondat: IMondat = sampleWithRequiredData;
        expectedResult = service.addMondatToCollectionIfMissing([], mondat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mondat);
      });

      it('should not add a Mondat to an array that contains it', () => {
        const mondat: IMondat = sampleWithRequiredData;
        const mondatCollection: IMondat[] = [
          {
            ...mondat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMondatToCollectionIfMissing(mondatCollection, mondat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mondat to an array that doesn't contain it", () => {
        const mondat: IMondat = sampleWithRequiredData;
        const mondatCollection: IMondat[] = [sampleWithPartialData];
        expectedResult = service.addMondatToCollectionIfMissing(mondatCollection, mondat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mondat);
      });

      it('should add only unique Mondat to an array', () => {
        const mondatArray: IMondat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mondatCollection: IMondat[] = [sampleWithRequiredData];
        expectedResult = service.addMondatToCollectionIfMissing(mondatCollection, ...mondatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mondat: IMondat = sampleWithRequiredData;
        const mondat2: IMondat = sampleWithPartialData;
        expectedResult = service.addMondatToCollectionIfMissing([], mondat, mondat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mondat);
        expect(expectedResult).toContain(mondat2);
      });

      it('should accept null and undefined values', () => {
        const mondat: IMondat = sampleWithRequiredData;
        expectedResult = service.addMondatToCollectionIfMissing([], null, mondat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mondat);
      });

      it('should return initial array if no Mondat is added', () => {
        const mondatCollection: IMondat[] = [sampleWithRequiredData];
        expectedResult = service.addMondatToCollectionIfMissing(mondatCollection, undefined, null);
        expect(expectedResult).toEqual(mondatCollection);
      });
    });

    describe('compareMondat', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMondat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21136 };
        const entity2 = null;

        const compareResult1 = service.compareMondat(entity1, entity2);
        const compareResult2 = service.compareMondat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21136 };
        const entity2 = { id: 21971 };

        const compareResult1 = service.compareMondat(entity1, entity2);
        const compareResult2 = service.compareMondat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21136 };
        const entity2 = { id: 21136 };

        const compareResult1 = service.compareMondat(entity1, entity2);
        const compareResult2 = service.compareMondat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
