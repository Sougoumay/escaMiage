const puppeteer = require('puppeteer');

(async () => {
    // Lance le navigateur en mode headless
    const browser = await puppeteer.launch({
        headless: 'new', // évite certains bugs liés à la version Chromium
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    const page = await browser.newPage();

    // L'URL de ta page Docusaurus (remplace si besoin)
    const url = 'http://localhost:3000/docs/plan-du-rapport/Initialisation du projet';

    // Accède à la page
    await page.goto(url, {
        waitUntil: 'networkidle0',
        timeout: 0 // option pour éviter les erreurs si la page met du temps à charger
    });

    // Génère le PDF
    await page.pdf({
        path: 'documentation-init.pdf',
        format: 'A4',
        printBackground: true,
        margin: { top: '20mm', bottom: '20mm', left: '15mm', right: '15mm' }
    });

    await browser.close();
    console.log('✅ PDF généré avec succès : documentation.pdf');
})();
