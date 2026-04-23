describe('Guest flow - read only access', () => {

  it('can only see published articles', () => {

    cy.visit('/');

    // articles visibles
    cy.contains('Subway').should('be.visible');

    // pas accès fonctionnalités
    cy.contains('New').should('not.exist');
    cy.contains('Review').should('not.exist');
    cy.contains('Admin').should('not.exist');
  });

});
