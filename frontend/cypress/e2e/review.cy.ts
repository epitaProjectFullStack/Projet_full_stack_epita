describe('Review Page', () => {
  beforeEach(() => {
    // La page /new nécessite d'être connecté
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');
    cy.get('input[value="Login"]').click();
    cy.url().should('include', '/');
    cy.visit('/reviewer');
  });
  it('load new page', () => {
     cy.url().should('include', '/reviewer');
  });
});

