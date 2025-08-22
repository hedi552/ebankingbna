import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Mondat e2e test', () => {
  const mondatPageUrl = '/mondat';
  const mondatPageUrlPattern = new RegExp('/mondat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mondatSample = { compteSrc: 'friendly who throughout', compteBenef: 'consequently', montant: 32009.64 };

  let mondat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mondats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mondats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mondats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mondat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mondats/${mondat.id}`,
      }).then(() => {
        mondat = undefined;
      });
    }
  });

  it('Mondats menu should load Mondats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mondat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Mondat').should('exist');
    cy.url().should('match', mondatPageUrlPattern);
  });

  describe('Mondat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mondatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Mondat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mondat/new$'));
        cy.getEntityCreateUpdateHeading('Mondat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mondatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mondats',
          body: mondatSample,
        }).then(({ body }) => {
          mondat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mondats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mondat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mondatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Mondat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mondat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mondatPageUrlPattern);
      });

      it('edit button click should load edit Mondat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mondat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mondatPageUrlPattern);
      });

      it('edit button click should load edit Mondat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mondat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mondatPageUrlPattern);
      });

      it('last delete button click should delete instance of Mondat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mondat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mondatPageUrlPattern);

        mondat = undefined;
      });
    });
  });

  describe('new Mondat page', () => {
    beforeEach(() => {
      cy.visit(`${mondatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Mondat');
    });

    it('should create an instance of Mondat', () => {
      cy.get(`[data-cy="compteSrc"]`).type('zowie ack');
      cy.get(`[data-cy="compteSrc"]`).should('have.value', 'zowie ack');

      cy.get(`[data-cy="compteBenef"]`).type('swerve who well');
      cy.get(`[data-cy="compteBenef"]`).should('have.value', 'swerve who well');

      cy.get(`[data-cy="montant"]`).type('21128.57');
      cy.get(`[data-cy="montant"]`).should('have.value', '21128.57');

      cy.get(`[data-cy="code"]`).type('pasta');
      cy.get(`[data-cy="code"]`).should('have.value', 'pasta');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        mondat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', mondatPageUrlPattern);
    });
  });
});
