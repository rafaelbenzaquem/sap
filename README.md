##
![](https://img.shields.io/github/issues/rafaelbenzaquem/sap)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/rafaelbenzaquem/sap)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/rafaelbenzaquem/sap)
![](https://img.shields.io/github/stars/rafaelbenzaquem/sap)

## :speech_balloon:O que há neste documento
1. [Missão deste projeto](./README.md#hammermissão-deste-projeto)
2. [Estrutura do projeto](./README.md#rocketestrutura-do-projeto)
3. [Endpoints disponíveis](./README.md#mag_rightendpoints-disponíveis)

## :hammer:Missão deste projeto

Na Justiça Federal de Roraima, cada servidor precisa ao final cada mês apresentar o relatório de controle de ponto que 
é preenchido manualmente no decorrer do mês. A partir disso foi desenvolvido uma solução back-end para fazer o 
controle de ponto de usuário, emitir o relatório mensal de frequência, tudo a partir do Sistema de controle de acesso da 
Justiça Federal e integração com o Sistema JF1. Essa solução foi desenvolvida para execução em containeres, provendo 
recursos a partir de uma API RESTFull:


## :rocket:Estrutura do projeto

O projeto foi estruturado de maneira simples para facilitar o entendimento de novos colaboradores. Cada pacote principal representa um domínio da aplicação, uma alusão ao Domain Driven Design. Cada pacote principal pode conter subpacotes com funcionalidades, complementos ou componentes específicos do seu domínio.


>- /desafio-votos		# raiz do projeto.
>>- /analise	        # pacote com serviço externo de análise de cpf.
>>- /associado	# pacote de classes para cadastro de associados.
>>- /commons	# pacote de classes comuns a todos os domínios.
>>- /pauta		# pacote de classes para cadastro de pautas.
>>- /resultado  # pacotes de classes de serviços de resultado de votação
>>- /sessão		# pacote de classes para criação de sessões.
>>- /voto		# pacotes de classes de serviços de votos.



## :mag_right:Endpoints disponíveis

Antecedido por http://\<seu-host\>:8080 temos os endpoints

- /v1/associados \(POST\)
- /v1/pautas \(POST\)
- /v1/sessoes \(POST\)
- /v1/votos \(POST\)
- /v1/voto-resultado/pautas/{id_pauta} \(GET\)

- /swagger-ui.html \(Documentação da API\)