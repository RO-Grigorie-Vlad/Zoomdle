import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { LicentaUpdateComponent } from 'app/entities/licenta/licenta-update.component';
import { LicentaService } from 'app/entities/licenta/licenta.service';
import { Licenta } from 'app/shared/model/licenta.model';

describe('Component Tests', () => {
  describe('Licenta Management Update Component', () => {
    let comp: LicentaUpdateComponent;
    let fixture: ComponentFixture<LicentaUpdateComponent>;
    let service: LicentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [LicentaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LicentaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LicentaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LicentaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Licenta(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Licenta();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
