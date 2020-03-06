import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { AplicareConsultatieUpdateComponent } from 'app/entities/aplicare-consultatie/aplicare-consultatie-update.component';
import { AplicareConsultatieService } from 'app/entities/aplicare-consultatie/aplicare-consultatie.service';
import { AplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';

describe('Component Tests', () => {
  describe('AplicareConsultatie Management Update Component', () => {
    let comp: AplicareConsultatieUpdateComponent;
    let fixture: ComponentFixture<AplicareConsultatieUpdateComponent>;
    let service: AplicareConsultatieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [AplicareConsultatieUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AplicareConsultatieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AplicareConsultatieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AplicareConsultatieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AplicareConsultatie(123);
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
        const entity = new AplicareConsultatie();
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
