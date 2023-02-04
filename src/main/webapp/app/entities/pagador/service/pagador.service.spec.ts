import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPagador } from '../pagador.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pagador.test-samples';

import { PagadorService } from './pagador.service';

const requireRestSample: IPagador = {
  ...sampleWithRequiredData,
};

describe('Pagador Service', () => {
  let service: PagadorService;
  let httpMock: HttpTestingController;
  let expectedResult: IPagador | IPagador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PagadorService);
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

    it('should create a Pagador', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pagador = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pagador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pagador', () => {
      const pagador = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pagador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pagador', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pagador', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pagador', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPagadorToCollectionIfMissing', () => {
      it('should add a Pagador to an empty array', () => {
        const pagador: IPagador = sampleWithRequiredData;
        expectedResult = service.addPagadorToCollectionIfMissing([], pagador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pagador);
      });

      it('should not add a Pagador to an array that contains it', () => {
        const pagador: IPagador = sampleWithRequiredData;
        const pagadorCollection: IPagador[] = [
          {
            ...pagador,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPagadorToCollectionIfMissing(pagadorCollection, pagador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pagador to an array that doesn't contain it", () => {
        const pagador: IPagador = sampleWithRequiredData;
        const pagadorCollection: IPagador[] = [sampleWithPartialData];
        expectedResult = service.addPagadorToCollectionIfMissing(pagadorCollection, pagador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pagador);
      });

      it('should add only unique Pagador to an array', () => {
        const pagadorArray: IPagador[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pagadorCollection: IPagador[] = [sampleWithRequiredData];
        expectedResult = service.addPagadorToCollectionIfMissing(pagadorCollection, ...pagadorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pagador: IPagador = sampleWithRequiredData;
        const pagador2: IPagador = sampleWithPartialData;
        expectedResult = service.addPagadorToCollectionIfMissing([], pagador, pagador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pagador);
        expect(expectedResult).toContain(pagador2);
      });

      it('should accept null and undefined values', () => {
        const pagador: IPagador = sampleWithRequiredData;
        expectedResult = service.addPagadorToCollectionIfMissing([], null, pagador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pagador);
      });

      it('should return initial array if no Pagador is added', () => {
        const pagadorCollection: IPagador[] = [sampleWithRequiredData];
        expectedResult = service.addPagadorToCollectionIfMissing(pagadorCollection, undefined, null);
        expect(expectedResult).toEqual(pagadorCollection);
      });
    });

    describe('comparePagador', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePagador(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePagador(entity1, entity2);
        const compareResult2 = service.comparePagador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePagador(entity1, entity2);
        const compareResult2 = service.comparePagador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePagador(entity1, entity2);
        const compareResult2 = service.comparePagador(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
