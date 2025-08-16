import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompteDetailComponent } from './compte-detail.component';

describe('Compte Management Detail Component', () => {
  let comp: CompteDetailComponent;
  let fixture: ComponentFixture<CompteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./compte-detail.component').then(m => m.CompteDetailComponent),
              resolve: { compte: () => of({ id: 21096 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CompteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load compte on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompteDetailComponent);

      // THEN
      expect(instance.compte()).toEqual(expect.objectContaining({ id: 21096 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
