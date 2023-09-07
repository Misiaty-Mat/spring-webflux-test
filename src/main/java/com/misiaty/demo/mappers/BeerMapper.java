package com.misiaty.demo.mappers;

import com.misiaty.demo.domain.Beer;
import com.misiaty.demo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDTO beerDTO);
}
