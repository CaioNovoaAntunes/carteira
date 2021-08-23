package br.com.orange.carteira.transacoes;

import br.com.orange.carteira.cartoes.CartaoRepository;
import br.com.orange.carteira.client.ValidCard;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/transacao")
public class TransacaoControlller {

    private  TransacaoRepository transacaoRepository;
    private final CartaoRepository cartaoRepository;
    private final ValidCard validCard;
    private final Logger logger = LoggerFactory.getLogger(TransacaoControlller.class);

    @Autowired
    public TransacaoControlller(TransacaoRepository transacaoRepository, CartaoRepository cartaoRepository, ValidCard validCard) {
        this.transacaoRepository = transacaoRepository;
        this.cartaoRepository = cartaoRepository;
        this.validCard = validCard;
    }

    @PostMapping
    @Transactional
    ResponseEntity<?> cadastraTransacao(@RequestBody @Valid TransacaoRequest request) {
        Transacao transacao = request.paraTransacao(cartaoRepository);


        try{
            validCard.validarCartao(request.getCartao());
        }catch(FeignException e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("erro no Feign");
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("erro Exception");
        }

        transacaoRepository.save(transacao);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transacao.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleta(@PathVariable("id") Long id) {
        transacaoRepository.deleteById(id);
    return ResponseEntity.ok().build();
    }



    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualiza(@PathVariable("id") Long id, @RequestBody AtualizaTransacaoRequest atualizaTransacaoRequest) {
        Transacao transacao =  transacaoRepository.getOne(id);

        transacao.atualizaEstabelecimento(atualizaTransacaoRequest);
        transacaoRepository.save(transacao);
        return ResponseEntity.ok().build();
    }

}
