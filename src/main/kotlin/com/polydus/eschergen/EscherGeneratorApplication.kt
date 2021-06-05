package com.polydus.eschergen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EscherGeneratorApplication{

	companion object{
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<EscherGeneratorApplication>(*args)
		}
	}
}

