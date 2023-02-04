import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategoriaRecebimento } from '../categoria-recebimento.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../categoria-recebimento.test-samples';

import { CategoriaRecebimentoService } from './categoria-recebimento.service';

const requireRestSample: ICategoriaRecebimento = {
  ...sampleWithRequiredData,
};

describe('CategoriaRecebimento Service', () => {
  let service: CategoriaRecebimentoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICategoriaRecebimento | ICategoriaRecebimento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategoriaRecebimentoService);
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

    it('should create a CategoriaRecebimento', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const categoriaRecebimento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(categoriaRecebimento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategoriaRecebimento', () => {
      const categoriaRecebimento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(categoriaRecebimento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategoriaRecebimento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategoriaRecebimento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CategoriaRecebimento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCategoriaRecebimentoToCollectionIfMissing', () => {
      it('should add a CategoriaRecebimento to an empty array', () => {
        const categoriaRecebimento: ICategoriaRecebimento = sampleWithRequiredData;
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing([], categoriaRecebimento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaRecebimento);
      });

      it('should not add a CategoriaRecebimento to an array that contains it', () => {
        const categoriaRecebimento: ICategoriaRecebimento = sampleWithRequiredData;
        const categoriaRecebimentoCollection: ICategoriaRecebimento[] = [
          {
            ...categoriaRecebimento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing(categoriaRecebimentoCollection, categoriaRecebimento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategoriaRecebimento to an array that doesn't contain it", () => {
        const categoriaRecebimento: ICategoriaRecebimento = sampleWithRequiredData;
        const categoriaRecebimentoCollection: ICategoriaRecebimento[] = [sampleWithPartialData];
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing(categoriaRecebimentoCollection, categoriaRecebimento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaRecebimento);
      });

      it('should add only unique CategoriaRecebimento to an array', () => {
        const categoriaRecebimentoArray: ICategoriaRecebimento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const categoriaRecebimentoCollection: ICategoriaRecebimento[] = [sampleWithRequiredData];
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing(categoriaRecebimentoCollection, ...categoriaRecebimentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categoriaRecebimento: ICategoriaRecebimento = sampleWithRequiredData;
        const categoriaRecebimento2: ICategoriaRecebimento = sampleWithPartialData;
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing([], categoriaRecebimento, categoriaRecebimento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaRecebimento);
        expect(expectedResult).toContain(categoriaRecebimento2);
      });

      it('should accept null and undefined values', () => {
        const categoriaRecebimento: ICategoriaRecebimento = sampleWithRequiredData;
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing([], null, categoriaRecebimento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaRecebimento);
      });

      it('should return initial array if no CategoriaRecebimento is added', () => {
        const categoriaRecebimentoCollection: ICategoriaRecebimento[] = [sampleWithRequiredData];
        expectedResult = service.addCategoriaRecebimentoToCollectionIfMissing(categoriaRecebimentoCollection, undefined, null);
        expect(expectedResult).toEqual(categoriaRecebimentoCollection);
      });
    });

    describe('compareCategoriaRecebimento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCategoriaRecebimento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCategoriaRecebimento(entity1, entity2);
        const compareResult2 = service.compareCategoriaRecebimento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCategoriaRecebimento(entity1, entity2);
        const compareResult2 = service.compareCategoriaRecebimento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCategoriaRecebimento(entity1, entity2);
        const compareResult2 = service.compareCategoriaRecebimento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
